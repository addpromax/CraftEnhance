package com.dutchjelly.craftenhance.gui.guis;

import com.dutchjelly.bukkitadapter.Adapter;
import com.dutchjelly.craftenhance.files.CategoryData;
import com.dutchjelly.craftenhance.files.MenuSettingsCache;
import com.dutchjelly.craftenhance.gui.guis.editors.RecipesViewerCategorysSettings;
import com.dutchjelly.craftenhance.gui.util.ButtonType;
import com.dutchjelly.craftenhance.gui.util.FormatListContents;
import com.dutchjelly.craftenhance.gui.util.GuiUtil;
import com.dutchjelly.craftenhance.gui.util.InfoItemPlaceHolders;
import com.dutchjelly.craftenhance.messaging.Messenger;
import com.dutchjelly.craftenhance.prompt.HandleChatInput;
import com.dutchjelly.craftenhance.util.PermissionTypes;
import org.broken.arrow.menu.button.manager.library.utility.MenuButtonData;
import org.broken.arrow.menu.button.manager.library.utility.MenuTemplate;
import org.broken.arrow.menu.library.button.MenuButton;
import org.broken.arrow.menu.library.button.logic.ButtonUpdateAction;
import org.broken.arrow.menu.library.button.logic.FillMenuButton;
import org.broken.arrow.menu.library.holder.MenuHolderPage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.dutchjelly.craftenhance.CraftEnhance.self;
import static com.dutchjelly.craftenhance.gui.util.GuiUtil.setTextItem;
import static com.dutchjelly.craftenhance.messaging.Messenger.Message;

public class RecipesViewerCategorys extends MenuHolderPage<CategoryData> {
	private final MenuSettingsCache menuSettingsCache = self().getMenuSettingsCache();
	private final MenuTemplate menuTemplate;

	public RecipesViewerCategorys(final String grupSeachFor) {
		super(FormatListContents.getCategorys(self().getCategoryDataCache().values(), grupSeachFor));
		this.menuTemplate = menuSettingsCache.getTemplate("RecipesCategorys");
		setFillSpace(this.menuTemplate.getFillSlots());
		setTitle(this.menuTemplate.getMenuTitle());
		//setIgnoreItemCheck(true);
		setMenuSize(GuiUtil.invSize("RecipesCategorys", this.menuTemplate.getAmountOfButtons()));
		setMenuOpenSound(this.menuTemplate.getSound());

		this.setUseColorConversion(true);
		this.setIgnoreItemCheck(true);
	}

	@Override
	public MenuButton getButtonAt(final int slot) {
		if (this.menuTemplate == null) return null;
		for (final Entry<List<Integer>, MenuButtonData> menuTemplate : this.menuTemplate.getMenuButtons().entrySet()) {
			if (menuTemplate.getKey().contains(slot)) {
				return registerButtons(menuTemplate.getValue());
			}
		}
		return null;
	}


	private MenuButton registerButtons(final MenuButtonData value) {
		return new MenuButton() {
			@Override
			public void onClickInsideMenu(@Nonnull final Player player, @Nonnull final Inventory menu, @Nonnull final ClickType click, @Nonnull final ItemStack clickedItem) {
				if (run(value, menu, player, click))
					updateButtons();
			}

			@Override
			public ItemStack getItem() {
				org.broken.arrow.menu.button.manager.library.utility.MenuButton button = null;
				if (getViewer().hasPermission(PermissionTypes.Categorys_editor.getPerm()))
					button = value.getActiveButton();
				if (button == null)
					button = value.getPassiveButton();

				return Adapter.getItemStack(button.getMaterial(), button.getDisplayName(), button.getLore(), button.getExtra(), button.isGlow());
			}
		};
	}

	public boolean run(final MenuButtonData value, final Inventory menu, final Player player, final ClickType click) {
		if (value.isActionTypeEqual(ButtonType.PrvPage.name())) {
			previousPage();
			return true;
		}
		if (value.isActionTypeEqual(ButtonType.NxtPage.name())) {
			nextPage();
			return true;
		}
		if (value.isActionTypeEqual(ButtonType.Search.name())) {
			if (click == ClickType.RIGHT) {
				Messenger.Message("Search for categorys.", getViewer());
				new HandleChatInput(this, msg -> {
					if (GuiUtil.seachCategory(msg)) {
						new RecipesViewerCategorys(msg).menuOpen(getViewer());
						return false;
					}
					return true;
				}).setMessages("Search for categorys.")
						.start(getViewer());
				;
			} else new RecipesViewerCategorys("").menuOpen(player);
		}

		if (value.isActionTypeEqual(ButtonType.NewCategory.name()) && player.hasPermission(PermissionTypes.Categorys_editor.getPerm())) {
			if (!player.isConversing()) {
				new HandleChatInput(this, msg -> {
					if (!GuiUtil.newCategory(msg, player)) {
						new RecipesViewerCategorys("").menuOpen(player);
						return false;
					}
					return true;
				}).setMessages("Please input your category name and item type you want. Like this 'category' without '.Type cancel, quit, exit or q to close this without change.")
						.start(getViewer());
			} else {
				Message("You still conversing, type cancel, quit, exit or q to close in chat and enter", player);
			}
		}
		return false;
	}

	@Override
	public FillMenuButton<CategoryData> createFillMenuButton() {
		return new FillMenuButton<>((player1, inventory, clickType, itemStack, categoryData) -> {
			if (categoryData != null) {
				if (clickType == ClickType.LEFT)
					new RecipesViewer(categoryData, "", player).menuOpen(player);
				else if (player.hasPermission(PermissionTypes.Categorys_editor.getPerm())) {
					new RecipesViewerCategorysSettings(categoryData.getRecipeCategory()).menuOpen(player);
				}
			}
			return ButtonUpdateAction.NONE;
		}, (slot, categoryData) -> {
			if (categoryData != null) {
				String displayName = " ";
				List<String> lore = new ArrayList<>();
				final Map<String, String> placeHolders = new HashMap<>();
				if (menuTemplate != null) {
					final MenuButtonData menuButton = menuTemplate.getMenuButton(-1);
					if (menuButton != null) {
						displayName = menuButton.getPassiveButton().getDisplayName();
						lore = menuButton.getPassiveButton().getLore();
					}
				}
				final ItemStack itemStack = categoryData.getRecipeCategoryItem();
				setTextItem(itemStack, displayName, lore);
				String categoryName = categoryData.getDisplayName();
				if (categoryName == null || categoryName.equals(""))
					categoryName = categoryData.getRecipeCategory();
				placeHolders.put(InfoItemPlaceHolders.DisplayName.getPlaceHolder(), categoryName);
				return GuiUtil.ReplaceAllPlaceHolders(itemStack.clone(), placeHolders);
			}
			return null;
		});
	}
}
